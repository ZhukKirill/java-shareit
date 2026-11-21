package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment toComment(CommentRequestDto commentRequest, User author, Long itemId,
                                    LocalDateTime created) {
        Item item = new Item();
        item.setId(itemId);

        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(created);
        return comment;
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }
}