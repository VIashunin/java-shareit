package ru.practicum.server.comment.mapper;

import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {
    public static Comment mapFromCommentDtoToComment(CommentDto commentDto, User user, Item item) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(item)
                .author(user)
                .created(commentDto.getCreated())
                .build();
    }

    public static CommentDto mapFromCommentToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDto> mapFromCommentListToCommentDtoList(List<Comment> commentList) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(mapFromCommentToCommentDto(comment));
        }
        return commentDtoList;
    }
}
