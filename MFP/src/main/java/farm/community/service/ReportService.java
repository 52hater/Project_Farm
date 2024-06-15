package farm.community.service;

import farm.community.domain.Post;
import farm.community.domain.Report;
import farm.community.repository.PostRepository;
import farm.community.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    private final PostRepository postRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, PostRepository postRepository) {
        this.reportRepository = reportRepository;
        this.postRepository = postRepository;
    }

    public void report(String reason, long postId) {
        Post post = checkPost(postId);
        createReport(reason, post);
    }

    private Post checkPost(long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("해당 게시글이 존재하지 않습니다."));
    }

    private void createReport(String reason, Post post){
        Report report = Report.createReport(reason, post);
        reportRepository.save(report);
    }

}
