/*
 * Copyright (c) 2021 created by Computer Engineering students (Cesar Marrote Manzano,
 * Christopher de Oliveira Souza and Murilo de Paula Araujo) at PUC-Campinas.
 *
 * All rights reserved.
 */

/* Responsável pela execução do programa */

package com.example.virtualmachine;

import codeTableView.Commands;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import stackTableView.MemoryStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class VirtualMachine {

    CharSequence charSequence;
    List<Commands> commands;
    LinkedList<Integer> stack;
    private String textAreaResultString = "";
    private final TextInputDialog textInputDialog;
    private final Alert endOfExecutionAlert;
    private final TextArea resultTextArea;
    private final Button nextStep;
    private final TableView<MemoryStack> stackTable;
    int i = 0, s = 0;

    public void prepareFile(String path) throws IOException {
        char[] file = Files.readString(Paths.get(path)).toCharArray();
        charSequence = new StringBuilder(String.valueOf(file).replaceAll("\\r\\n", ""));
        prepareInstruction();
    }

    public VirtualMachine(List<Object> objects) {
        this.textInputDialog = (TextInputDialog) objects.get(0);
        this.resultTextArea = (TextArea) objects.get(1);
        this.endOfExecutionAlert = (Alert) objects.get(2);
        this.nextStep = (Button) objects.get(3);
        this.stackTable = (TableView<MemoryStack>) objects.get(4);
        stack = new LinkedList<>();
        for (int i = 0; i < 500; i++) {
            stack.add(i, null);
        }
    }

    public void cleanDate() {
        i = 0;
        s = 0;
        stack.clear();
        textAreaResultString = "";
        resultTextArea.setText(textAreaResultString);
        for (int j = 0; j < 500; j++) {
            stack.add(j, null);
        }
    }

    public Integer getPosition() {
        return i;
    }

    public List<Commands> getCommands() {
        return commands;
    }

    public void updateStackOnTheScreen() {
        List<MemoryStack> memoryStackList = new ArrayList<>();
        for (int i = 0; i <= s; i++) {
            memoryStackList.add(new MemoryStack(stack.get(i) == null ? "" : stack.get(i).toString(), i));
        }
        stackTable.setItems(FXCollections.observableList(memoryStackList));
    }

    //Ajusta o valor (identificador) do jump, para que as intruções JMP, JMPF e CALL sejam executadas corretamente
    public void adjustJump() {
        commands.forEach(commands -> {
            if (commands.getInstruction2().contains("JMP") || commands.getInstruction2().contains("JMPF") || commands.getInstruction2().contains("CALL")) {
                Optional<Commands> label = this.commands.stream().filter(commands1 ->
                        commands1.getInstruction1().replace(" ", "").equals(commands.getInstruction3().replace(" ", ""))
                ).findFirst();
                label.ifPresent(value -> commands.setInstruction3(String.valueOf(value.getRow()).trim()));
            }
        });

        commands.forEach(commands -> {
            if (commands.getInstruction2().contains("NULL")) {
                commands.setInstruction1(String.valueOf(commands.getRow()).trim());
            }
        });
    }

    //Prepara intruções a partir do arquivo lido
    public void prepareInstruction() {
        commands = new ArrayList<>();
        int i = 0;
        while (charSequence.length() >= i + 32) {
            String instruction = String.valueOf(charSequence.subSequence(i, i + 32));
            Commands commands = new Commands();
            commands.setInstruction1(String.valueOf(instruction.subSequence(0, 8)).trim());
            commands.setInstruction2(String.valueOf(instruction.subSequence(8, 16)).trim());
            commands.setInstruction3(String.valueOf(instruction.subSequence(16, 24)).trim());
            commands.setInstruction4(String.valueOf(instruction.subSequence(24, 32)).trim());
            commands.setRow((i / 32));
            this.commands.add(commands);
            i += 32;
        }
        adjustJump();
        commands.forEach(commands -> System.out.println(commands.getInstruction1() + commands.getInstruction2() + commands.getInstruction3() + commands.getInstruction4()));
    }

    // Verifica o comando atual e executa
    public void analiseCommand() {
        switch (commands.get(i).getInstruction2().trim()) {
            case "START" -> s = -1;
            case "DALLOC" -> {
                int m = Integer.parseInt(commands.get(i).getInstruction3().replace(" ", ""));
                int n = Integer.parseInt(commands.get(i).getInstruction4().replace(" ", ""));
                for (int k = n - 1; k >= 0; k--) {
                    stack.set(m + k, stack.get(s));
                    s--;
                }
            }
            case "ALLOC" -> {
                int m = Integer.parseInt(commands.get(i).getInstruction3().replace(" ", ""));
                int n = Integer.parseInt(commands.get(i).getInstruction4().replace(" ", ""));
                for (int k = 0; k <= n - 1; k++) {
                    s++;
                    stack.set(s, stack.get(m + k));
                }
            }
            case "STR" -> {
                int n = Integer.parseInt(commands.get(i).getInstruction3().replace(" ", ""));
                stack.set(n, stack.get(s));
                s--;
            }
            case "RD" -> {
                s++;
                Optional<String> rdResult = textInputDialog.showAndWait();
                if (rdResult.isPresent()) {
                    stack.set(s, Integer.parseInt(rdResult.get()));
                    textInputDialog.getEditor().clear();
                }
            }
            case "PRN" -> {
                textAreaResultString += stack.get(s).toString() + "\n";
                resultTextArea.setText(textAreaResultString);
                s--;
            }
            case "CALL" -> {
                int p = Integer.parseInt(commands.get(i).getInstruction3().replace(" ", ""));
                s++;
                stack.set(s, i);// ou i++???

                i = p;
            }
            case "LDC" -> {
                int k = Integer.parseInt(commands.get(i).getInstruction3().replace(" ", ""));
                s++;
                stack.set(s, k);
            }
            case "LDV" -> {
                int n = Integer.parseInt(commands.get(i).getInstruction3().replace(" ", ""));
                s++;
                stack.set(s, stack.get(n));
            }
            case "ADD" -> {
                stack.set(s - 1, stack.get(s - 1) + stack.get(s));
                s--;
            }
            case "SUB" -> {
                stack.set(s - 1, stack.get(s - 1) - stack.get(s));
                s--;
            }
            case "MULT" -> {
                stack.set(s - 1, stack.get(s - 1) * stack.get(s));
                s--;
            }
            case "DIVI" -> {
                stack.set(s - 1, stack.get(s - 1) / stack.get(s));
                s--;
            }
            case "INV" -> stack.set(s, -stack.get(s));
            case "AND" -> {
                if (stack.get(s - 1) == 1 && stack.get(s) == 1) {
                    stack.set(s - 1, 1);
                } else {
                    stack.set(s - 1, 0);
                }
                s--;
            }
            case "OR" -> {
                if (stack.get(s - 1) == 1 || stack.get(s) == 1) {
                    stack.set(s - 1, 1);
                } else {
                    stack.set(s - 1, 0);
                }
                s--;
            }
            case "NEG" -> stack.set(s, 1 - stack.get(s));
            case "CME" -> {
                if (stack.get(s - 1) < stack.get(s)) {
                    stack.set(s - 1, 1);
                } else {
                    stack.set(s - 1, 0);
                }
                s--;
            }
            case "CMA" -> {
                if (stack.get(s - 1) > stack.get(s)) {
                    stack.set(s - 1, 1);
                } else {
                    stack.set(s - 1, 0);
                }
                s--;
            }
            case "CEQ" -> {
                if (Objects.equals(stack.get(s - 1), stack.get(s))) {
                    stack.set(s - 1, 1);
                } else {
                    stack.set(s - 1, 0);
                }
                s--;
            }
            case "CDIF" -> {
                if (!Objects.equals(stack.get(s - 1), stack.get(s))) {
                    stack.set(s - 1, 1);
                } else {
                    stack.set(s - 1, 0);
                }
                s--;
            }
            case "CMEQ" -> {
                if (stack.get(s - 1) <= stack.get(s)) {
                    stack.set(s - 1, 1);
                } else {
                    stack.set(s - 1, 0);
                }
                s--;
            }
            case "CMAQ" -> {
                if (stack.get(s - 1) >= stack.get(s)) {
                    stack.set(s - 1, 1);
                } else {
                    stack.set(s - 1, 0);
                }
                s--;
            }
            case "JMP" -> i = Integer.parseInt(commands.get(i).getInstruction3().replace(" ", ""));
            case "JMPF" -> {
                int p = Integer.parseInt(commands.get(i).getInstruction3().replace(" ", ""));
                if (stack.get(s) == 0)
                    i = p;
                s = s - 1;
            }
            case "RETURN" -> {
                i = stack.get(s);
                s--;
            }
            case "HLT" -> {
                nextStep.setVisible(false);
                endOfExecutionAlert.showAndWait();
            }
        }
        updateStackOnTheScreen();
        i++;
    }

    public void run() {
        while (!commands.get(i).getInstruction2().trim().equals("HLT")) {
            analiseCommand();
        }
        endOfExecutionAlert.showAndWait();
    }
}
