public String translate(String word){
	HashMap<String, String> s = initData.getHashMap();
	if (s.containsKey(word)) {
		return word;
	}
	return s.get(word);
}