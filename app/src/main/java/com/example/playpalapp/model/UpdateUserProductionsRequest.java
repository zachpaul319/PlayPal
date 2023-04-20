package com.example.playpalapp.model;

public class UpdateUserProductionsRequest {
    String currentProduction, pastProductions;

    public UpdateUserProductionsRequest(String currentProduction, String pastProductions) {
        this.currentProduction = currentProduction;
        this.pastProductions = pastProductions;
    }
}
