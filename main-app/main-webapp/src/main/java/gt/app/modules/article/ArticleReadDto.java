package gt.app.modules.article;

import gt.app.domain.ArticleStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
public class ArticleReadDto {

    private Long id;

    private String title;

    private ArticleStatus status;

    private String content;

    private UUID userId;
    private String username;

    private Instant createdDate;

    private List<FileInfo> files = new ArrayList<>();

    private List<CommentDto> comments = new LinkedList<>();

    @Data
    @NoArgsConstructor
    public static class FileInfo {
        UUID id;
        String name;
    }


    @Data
    @NoArgsConstructor
    public static class CommentDto {
        Long id;
        String content;

        UUID userId;
        String username;

        Instant createdDate;

        Long articleId;
        Long parentCommentId;

        List<CommentDto> childComments = new LinkedList<>();
    }
}
