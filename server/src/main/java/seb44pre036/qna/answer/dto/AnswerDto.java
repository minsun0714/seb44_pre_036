package seb44pre036.qna.answer.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seb44pre036.qna.member.entity.Member;
import seb44pre036.qna.question.entity.Question;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class AnswerDto {

    @Getter
    @Setter
    public static class Response {
        private long answerId;

        private String content;

        private String isAccepted;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private int vote;

        private long questionId;

        private long memberId;

    }

    @Getter
    @Setter
    public static class Post {

        //@NotBlank(message="맴버 ID 입력이 필요합니다")
        private long memberId;

        @NotBlank(message="질문 ID 입력이 필요합니다")
        private long questionId;

        @NotBlank(message = "답변 내용을 입력해주세요.")
        private String content;

    }

    @Getter
    @Setter
    public static class Patch {
        //@NotBlank(message="사용자 ID 입력이 필요합니다")
        private long memberId;

        @NotBlank(message = "답변 id가 필요합니다.")
        private long answerId;

        @NotBlank(message = "내용은 공백이 아니어야 합니다.")
        private String content;

    }

    @Getter
    @Setter
    public static class Select {
        //@NotBlank(message = "현재 사용자 정보를 입력해주세요")
        private long memberId;

        @NotBlank(message = "채택할 질문 정보를 입력해주세요")
        private long answerId;
    }




}