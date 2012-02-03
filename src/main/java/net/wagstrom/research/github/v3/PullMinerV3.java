package net.wagstrom.research.github.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullMinerV3 {
	private PullRequestService service;
	
	private Logger log;
	
	public PullMinerV3(GitHubClient ghc) {
		service = new PullRequestService(ghc);
		log = LoggerFactory.getLogger(PullMinerV3.class);
	}
	
	public Collection<PullRequest> getOpenPullRequests(IRepositoryIdProvider repository) {
		try {
			return service.getPullRequests(repository, IssueService.STATE_OPEN);
		} catch (IOException e) {
			log.error("IOException in getOpenPullRequests {} {}", new Object[]{repository.generateId(), e});
			return null;
		}
	}
	
	public Collection<PullRequest> getClosedPullRequests(IRepositoryIdProvider repository) {
		try {
			return service.getPullRequests(repository, IssueService.STATE_CLOSED);
		} catch (IOException e) {
			log.error("IOException in getOpenPullRequests {} {}", new Object[]{repository.generateId(), e});
			return null;
		}
	}
	
	public Collection<PullRequest> getAllPullRequests(IRepositoryIdProvider repository) {
		Collection<PullRequest> openIssues = getOpenPullRequests(repository);
		Collection<PullRequest> closedIssues = getClosedPullRequests(repository);
		// simple hack to check if openIssues returned a null set
		if (openIssues != null) {
			openIssues.addAll(closedIssues);
			return openIssues;
		} else {
			return closedIssues;
		}
	}
	
	public PullRequest getPullRequest(IRepositoryIdProvider repository, int id) {
		try {
			return service.getPullRequest(repository, id);
		} catch (IOException e) {
			log.error("IO Exception fetching PullRequest {}:{}", new Object[]{repository.generateId(), id,  e});
			return null;
		}
	}
	
	public List<CommitComment> getComments(IRepositoryIdProvider repository, int id) {
		try {
			return service.getComments(repository, id);
		} catch (IOException e) {
			log.error("IO Exception fetching comments {}:{}", new Object[]{repository.generateId(), id,  e});
			return null;
		}
	}

}