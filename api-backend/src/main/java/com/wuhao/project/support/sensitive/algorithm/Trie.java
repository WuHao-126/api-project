package com.wuhao.project.support.sensitive.algorithm;

import com.wuhao.project.support.sensitive.TextWorker;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wuhao
 * @Datetime: TODO
 * @Description: 前缀树实现
 */
public class Trie implements TextWorker {

    private TrieNode root;

    public Trie(){
        root= new TrieNode();
    }

    @Override
    public void insert(String word){
        TrieNode next=root;
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(!next.children.containsKey(chars[i])){
                   next.children.put(chars[i],new TrieNode());
            }
            next=next.children.get(chars[i]);
        }
        next.isWord=true;
    }
    @Override
    public Boolean search(String word){
        TrieNode node = root;
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(!node.children.containsKey(chars[i])){
                return false;
            }
            node=node.children.get(chars[i]);
        }
        return node.isWord;
    }
    //序列化敏感词字符串
    // Serialize the trie to a string
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb, new StringBuilder());
        return sb.toString();
    }

    private void serializeHelper(TrieNode node, StringBuilder sb, StringBuilder currentWord) {
        if (node == null) return;
        for (char ch : node.children.keySet()) {
            currentWord.append(ch);
            if (node.children.get(ch).isWord) {
                sb.append(currentWord).append("#*"); // 敏感词的结尾标记为 #
            } else {
                serializeHelper(node.children.get(ch), sb, currentWord);
            }
            currentWord.deleteCharAt(currentWord.length() - 1);
        }
    }
    //反序列化敏感词
    public void deserialize(String serialized) {
        String[] words = serialized.split("\\*");
        for (String word : words) {
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                if (ch=='#'){
                    break;
                }
                if (!node.children.containsKey(ch)) {
                    node.children.put(ch, new TrieNode());
                }
                node = node.children.get(ch);
            }
            node.isWord = word.endsWith("#");
        }
    }
    //文本匹配
    @Override
    public Boolean containsSensitiveWord(String text) {
        for (int i = 0; i < text.length(); i++) {
            TrieNode node = root;
            int j = i;
            while (j < text.length() && node.children.containsKey(text.charAt(j))) {
                node = node.children.get(text.charAt(j));
                if (node.isWord) {
                    return true; // 敏感词匹配成功
                }
                j++;
            }
        }
        return false; // 敏感词未找到
    }

    class TrieNode{
        private Map<Character, TrieNode> children;
        private Boolean isWord;

        public TrieNode() {
            this.children = new HashMap<>();
            this.isWord = false;
        }
    }
}
