package com.epam.project.dao;

public enum DataBaseSelector {
    MY_SQL {
        @Override
        public String toString() {
            return "MySQL";
        }
    }
}
