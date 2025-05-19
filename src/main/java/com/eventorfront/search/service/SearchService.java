package com.eventorfront.search.service;

import java.util.List;

public interface SearchService {

	List<String> getTopKeywords();

	List<String> getKeywords();

	void deleteKeyword(String keyword);

}
