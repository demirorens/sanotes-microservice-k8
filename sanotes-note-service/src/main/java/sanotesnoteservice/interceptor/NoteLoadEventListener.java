package sanotesnoteservice.interceptor;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sanotesnoteservice.exeption.ResourceNotFoundException;
import sanotesnoteservice.model.NoteContainerModel;
import sanotesnoteservice.model.NoteModel;
import sanotesnoteservice.model.NoteVersionModel;
import sanotesnoteservice.repository.NoteRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Component
public class NoteLoadEventListener implements PostLoadEventListener {

    @Autowired
    private transient NoteRepository noteRepository;

    @Autowired
    private transient EntityManagerFactory entityManagerFactory;

    @PostConstruct
    private void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_LOAD).appendListener(this);
    }

    @Override
    public void onPostLoad(PostLoadEvent postLoadEvent) {
        final Object entity = postLoadEvent.getEntity();
        if (entity instanceof NoteContainerModel
                && ((NoteContainerModel) entity).getNoteId() != null
                && ((NoteContainerModel) entity).getTopic() == null
                && ((NoteContainerModel) entity).getText() == null) {
            NoteModel notModel = noteRepository.findById(((NoteContainerModel) entity).getNoteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Note", "by id", ((NoteContainerModel) entity).getNoteId()));
            ((NoteContainerModel) entity).setTopic(notModel.getTopic());
            ((NoteContainerModel) entity).setText(notModel.getText());

        } else if (entity instanceof NoteVersionModel
                && ((NoteVersionModel) entity).getNoteId() != null
                && ((NoteVersionModel) entity).getTopic() == null
                && ((NoteVersionModel) entity).getText() == null) {
            NoteModel notModel = noteRepository.findById(((NoteVersionModel) entity).getNoteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Note", "by id", ((NoteVersionModel) entity).getNoteId()));
            ((NoteVersionModel) entity).setTopic(notModel.getTopic());
            ((NoteVersionModel) entity).setText(notModel.getText());

        }
    }

}
