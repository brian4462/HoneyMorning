package com.jica.honeymorning.database;


import com.jica.honeymorning.database.entity.RecommendedList;
import com.jica.honeymorning.database.entity.TodoValue;

import java.util.List;

public class OneDayValue {
    private String day;
    private List<TodoValue> todoValues;
    private List<RecommendedList> recommendedLists;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<TodoValue> getTodoValueList() {
        return todoValues;
    }

    public void setTodoValue(List<TodoValue> todoValues) {
        this.todoValues = todoValues;
    }

    public List<RecommendedList> getRecommendedLists() {
        return recommendedLists;
    }

    public void setRecommendedLists(List<RecommendedList> recommendedLists) {
        this.recommendedLists = recommendedLists;
    }
}
