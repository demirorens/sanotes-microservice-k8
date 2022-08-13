package sanotestagservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sanotestagservice.exception.ResourceNotFoundException;
import sanotestagservice.exception.UnauthorizedException;
import sanotestagservice.model.TagModel;
import sanotestagservice.payload.ApiResponse;
import sanotestagservice.payload.ByIdRequest;
import sanotestagservice.repository.TagRepository;
import sanotestagservice.service.TagService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private static final String USER_DONT_HAVE_PERMISSION = "User don't have permission for this request";

    public TagModel saveTag(TagModel tagModel) {
        return tagRepository.save(tagModel);
    }

    public TagModel updateTag(TagModel tagModel, String userId) {
        TagModel oldTagModel = tagRepository.findById(tagModel.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "by id", tagModel.getId().toString()));
        if (!oldTagModel.getCreatedBy().toString().equals(userId))
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        oldTagModel.setName(tagModel.getName());
        oldTagModel.setDescription(tagModel.getDescription());
        return tagRepository.save(oldTagModel);
    }

    public TagModel getTag(UUID id, String userId) {
        TagModel tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "by id", id.toString()));
        if (!tag.getCreatedBy().toString().equals(userId))
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        return tag;
    }

    @Override
    public List<TagModel> getUserTags(String userId) {
        List<TagModel> tags = tagRepository.findByCreatedByEquals(UUID.fromString(userId));
        return tags;
    }

    public ApiResponse deleteTag(ByIdRequest byIdRequest, String userId) {
        TagModel tag = tagRepository.findById(byIdRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "by id", byIdRequest.getId().toString()));
        if (!tag.getCreatedBy().toString().equals(userId))
            throw new UnauthorizedException(USER_DONT_HAVE_PERMISSION);
        tagRepository.delete(tag);
        return new ApiResponse(Boolean.TRUE, "You successfully delete tag ");
    }
}
