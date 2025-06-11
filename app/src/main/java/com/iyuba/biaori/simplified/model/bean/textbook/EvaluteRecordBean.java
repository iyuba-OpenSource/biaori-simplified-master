package com.iyuba.biaori.simplified.model.bean.textbook;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by iyuba on 2018/12/26.
 */

public class EvaluteRecordBean {


    @SerializedName("result")
    private String result;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataDTO data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("sentence")
        private String sentence;
        @SerializedName("total_score")
        private String totalScore;
        @SerializedName("word_count")
        private int wordCount;
        @SerializedName("URL")
        private String url;
        @SerializedName("words")
        private List<WordsDTO> words;

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(String totalScore) {
            this.totalScore = totalScore;
        }

        public int getWordCount() {
            return wordCount;
        }

        public void setWordCount(int wordCount) {
            this.wordCount = wordCount;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<WordsDTO> getWords() {
            return words;
        }

        public void setWords(List<WordsDTO> words) {
            this.words = words;
        }

        public static class WordsDTO {
            @SerializedName("content")
            private String content;
            @SerializedName("pron")
            private String pron;
            @SerializedName("index")
            private int index;
            @SerializedName("score")
            private String score;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPron() {
                return pron;
            }

            public void setPron(String pron) {
                this.pron = pron;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }
        }
    }
}
