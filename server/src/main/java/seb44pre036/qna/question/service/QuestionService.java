package seb44pre036.qna.question.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seb44pre036.qna.exception.BusinessLogicException;
import seb44pre036.qna.exception.ExceptionCode;
import seb44pre036.qna.member.entity.Member;
import seb44pre036.qna.member.service.MemberService;
import seb44pre036.qna.question.entity.Question;
import seb44pre036.qna.question.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberService memberService;

    public QuestionService(QuestionRepository questionRepository, MemberService memberService) {
        this.questionRepository = questionRepository;
        this.memberService = memberService;
    }

    public Question createQuestion(Question question, long authenticatedMemberId) {
        Member member = memberService.findVerifiedMember(authenticatedMemberId);
        question.setMember(member);

        return questionRepository.save(question);
    }

    public Question updateQuestion(Question question, long authenticatedMemberId) {
        Question findedQuestion = findVerifiedQuestion(question.getQuestionId());

        validateQuestionOwnership(findedQuestion, authenticatedMemberId);

        Optional.ofNullable(question.getTitle())
                .ifPresent(findedQuestion::setTitle);
        Optional.ofNullable(question.getContent())
                .ifPresent(findedQuestion::setContent);

        findedQuestion.setUpdatedAt(LocalDateTime.now());

        return findedQuestion;
    }

    @Transactional(readOnly = true)
    public Question findQuestion(long questionId) {
        Question findedQuestion = findVerifiedQuestion(questionId);
        findedQuestion.setViewCount(findedQuestion.getViewCount() + 1);

        return questionRepository.save(findedQuestion);
    }

    @Transactional(readOnly = true)
    public Page<Question> searchQuestions(int page, int size, String keyword){
        return questionRepository.findAllByTitleContainingIgnoreCase(keyword,
                PageRequest.of(page, size, Sort.by("questionId").descending()));
    }

    @Transactional(readOnly = true)
    public Page<Question> findQuestions(int page, int size, String tab) {
        if (tab.equals("View")) {
            tab = "viewCount";
        } else if (tab.equals("Newest")) {
            tab = "questionId";
        }

        return questionRepository.findAll(PageRequest.of(page, size, Sort.by(tab).descending()));

    }

    public void deleteQuestion(long questionId, long authenticatedMemberId) {
        Question findedQuestion = findVerifiedQuestion(questionId);

        validateQuestionOwnership(findedQuestion, authenticatedMemberId);

        questionRepository.delete(findedQuestion);
    }

    @Transactional(readOnly = true)
    public Question findVerifiedQuestion(long questionId) {

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        Question findedQuestion = optionalQuestion.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));

        return findedQuestion;
    }

    public void validateQuestionOwnership(Question question, long authenticatedMemberId) {
        if(!question.getMember().getMemberId().equals(authenticatedMemberId)) {
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }
    }
}
