// package com.eventorfront.search.scheduler;
//
// import java.util.List;
//
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import com.eventorfront.search.service.SearchService;
//
// import lombok.Getter;
// import lombok.RequiredArgsConstructor;
//
// @Component
// @RequiredArgsConstructor
// public class RankKeywordScheduler {
// 	private final SearchService searchService;
//
// 	@Getter
// 	private List<String> top10Keywords;
//
// 	@Scheduled(cron = "0 */5 * * * *") // 매 5분마다
// 	public void renewTop10Keywords() {
// 		top10Keywords = searchService.getTop10Keywords().getData();
// 	}
//
// }
