package com.parcinformatique.model;

public enum AssignmentStatus {
    ACTIVE,      // Active
    RETURNED;    // Retournée

    @Override
    public String toString() {
        return name();
    }
}