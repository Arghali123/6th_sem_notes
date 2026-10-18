package com.nepathya;

public class MultipleResultsSimulation {

    public static void main(String[] args) {

        // Imagine these are results received from a database:
        Object[] results = {
                "ResultSet: Sita, Ram",
                Integer.valueOf(2),
                "ResultSet: Java, Python"
        };

        boolean hasResultSet = true;

        for (int i = 0; i < results.length; i++) {

            if (hasResultSet) {
                System.out.println("Reading: " + results[i]);
            } else {
                int updateCount = (Integer) results[i];
                System.out.println(
                        "Rows affected: " + updateCount);
            }

            // Simulate moving to the next result.
            if (i + 1 < results.length) {
                hasResultSet = !(results[i + 1] instanceof Integer);
                System.out.println("demo:"+hasResultSet);
            } else {
                hasResultSet = false;
                System.out.println(
                        "No more results. Update count = -1");
            }
        }
    }
}
