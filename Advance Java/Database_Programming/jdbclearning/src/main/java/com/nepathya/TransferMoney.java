package com.nepathya;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransferMoney {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/studentdb";
        String username = "Daenish";
        String password = "Daenish";



        BigDecimal amount = new BigDecimal("1000.00");
        int fromAccount = 1;
        int toAccount = 2;

        try (Connection con = DriverManager.getConnection(url, username, password)) {

            con.setAutoCommit(false);

            try {
                // Debit the source account.
                String debitSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";

                try (PreparedStatement debit = con.prepareStatement(debitSql)) {
                    debit.setBigDecimal(1, amount);
                    debit.setInt(2, fromAccount);

                    if (debit.executeUpdate() != 1) {
                        throw new SQLException("Source account not found.");
                    }
                }

                // Credit the destination account.
                String creditSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

                try (PreparedStatement credit = con.prepareStatement(creditSql)) {
                    credit.setBigDecimal(1, amount);
                    credit.setInt(2, toAccount);

                    if (credit.executeUpdate() != 1) {
                        throw new SQLException("Destination account not found.");
                    }
                }

                con.commit();
                System.out.println("Transfer completed successfully.");

            } catch (SQLException e) {
                try {
                    con.rollback();
                    System.out.println("Transfer rolled back.");
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Database transaction failed.");
            e.printStackTrace();
        }
    }
}
