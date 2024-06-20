package org.owasp.webgoat.lessons.cryptography.mytest;

public class PathChecker {
    public static void main(String[] args) {
        String path = "..%2Fetc/passwd";
        System.out.println("Path: " + path);

        for (int i = 0; i < path.length(); i++) {
            System.out.println("Character at index " + i + ": " + (int) path.charAt(i));
        }

        if (!path.contains("./")) {
            System.out.println("Path does not contain './'");
        } else {
            System.out.println("Path contains './'");
        }
    }
}

