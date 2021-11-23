package com.example.virtualmachine;

import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class VirtualMachine {

    CharSequence charSequence;
    List<Comando> comandos;
    LinkedList<Integer> stack;
    private String textAreaResultString = "";
    private final TextInputDialog textInputDialog;
    private final Alert endOfExecutionAlert;
    private final TextArea resultTextArea;
    private final Button proximoPasso;
    private final TableView<Stack> stackTable;
    int i=0,s=0;

    public void prepararArquivo(String path) throws IOException {
        char[] arquivo = Files.readString(Paths.get(path)).toCharArray();

        charSequence = new StringBuilder(String.valueOf(arquivo).replaceAll("\\r\\n", ""));

        prepararInstrucao();
    }

    public VirtualMachine(List<Object> objects) {
        this.textInputDialog = (TextInputDialog) objects.get(0);
        this.resultTextArea = (TextArea) objects.get(1);
        this.endOfExecutionAlert = (Alert) objects.get(2);
        this.proximoPasso=(Button) objects.get(3);
        this.stackTable=(TableView<Stack>) objects.get(4);
        stack= new LinkedList<Integer>();
        for (int i = 0; i < 500; i++) {
            stack.add(i, null);
        }
    }

    public void limparDados(){
        i=0;
        s=0;
        stack.clear();
        textAreaResultString = "";
        resultTextArea.setText(textAreaResultString);
        for (int j = 0; j < 500; j++) {
            stack.add(j, null);
        }
    }

    public Integer getPosicao(){
        return i;
    }

    private String completar8(String string){
        while (string.length()!=8){
            string+=" ";
        }
        return string;
    }

    public List<Comando> getComandos(){
        return comandos;
    }

    public void atualizarPilhaTela(){
        List<Stack> stackList = new ArrayList<>();
        for(int i=0;i<=s;i++){
            stackList.add(new Stack(stack.get(i)==null?"":stack.get(i).toString(),i));
        }
        stackTable.setItems(FXCollections.observableList(stackList));
    }

    public void ajustarJump(){
        comandos.forEach(comando -> {
            if(comando.getInstrucao2().contains("JMP") || comando.getInstrucao2().contains("JPMF") || comando.getInstrucao2().contains("CALL")){
                Optional<Comando> rotulo =comandos.stream().filter(comando1 ->
                        comando1.getInstrucao1().replace(" ","").equals(comando.getInstrucao3().replace(" ",""))
                ).findFirst();
                if(rotulo.isPresent()) {
                    comando.setInstrucao3(completar8(String.valueOf(rotulo.get().getLinha())));
                }
            }
        });

        comandos.forEach(comando->{
            if(comando.getInstrucao2().contains("NULL")){
                comando.setInstrucao1(completar8(String.valueOf(comando .getLinha())));
            }
        });
    }

    public void prepararInstrucao(){
        comandos= new ArrayList<>();
        int i=0;
        while(charSequence.length()>=i+32){
                String instrucao=String.valueOf(charSequence.subSequence(i,i+32));
                Comando comando = new Comando();
                comando.setInstrucao1(String.valueOf(instrucao.subSequence(0,8)));
                comando.setInstrucao2(String.valueOf(instrucao.subSequence(8,16)));
                comando.setInstrucao3(String.valueOf(instrucao.subSequence(16,24)));
                comando.setInstrucao4(String.valueOf(instrucao.subSequence(24,32)));
                comando.setLinha((i/32));
                comandos.add(comando);
                i+=32;
        }
        ajustarJump();
        comandos.forEach(comando -> {
            System.out.println(comando.getInstrucao1()+comando.getInstrucao2()+comando.getInstrucao3()+comando.getInstrucao4());
        });
    }

    public void analisarComando(){
        if(comandos.get(i).getInstrucao2().trim().equals("START")){
            s=-1;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("DALLOC")){
            int m=Integer.parseInt(comandos.get(i).getInstrucao3().replace(" ",""));
            int n=Integer.parseInt(comandos.get(i).getInstrucao4().replace(" ",""));
            for(int k=n-1;k>=0;k--){
                stack.set(m+k,stack.get(s));
                s--;
            }
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("ALLOC")){
            int m=Integer.parseInt(comandos.get(i).getInstrucao3().replace(" ",""));
            int n=Integer.parseInt(comandos.get(i).getInstrucao4().replace(" ",""));
            for(int k=0;k<=n-1;k++){
                s++;
                stack.set(s,stack.get(m+k));
            }
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("STR")){
            int n=Integer.parseInt(comandos.get(i).getInstrucao3().replace(" ",""));
            stack.set(n,stack.get(s));
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("RD")){
            s++;
            Optional<String> rdResult = textInputDialog.showAndWait();
            if (rdResult.isPresent()) {
                stack.set(s, Integer.parseInt(rdResult.get()));
                textInputDialog.getEditor().clear();
            }
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("PRN")){
            textAreaResultString += stack.get(s).toString() + "\n";
            resultTextArea.setText(textAreaResultString);
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("CALL")){
            int p=Integer.parseInt(comandos.get(i).getInstrucao3().replace(" ",""));
            s++;
            stack.set(s,i);// ou i++???
            i=p;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("LDC")){
            int k=Integer.parseInt(comandos.get(i).getInstrucao3().replace(" ",""));
            s++;
            stack.set(s,k);
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("LDV")){
            int n=Integer.parseInt(comandos.get(i).getInstrucao3().replace(" ",""));
            s++;
            stack.set(s,stack.get(n));
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("ADD")){
            stack.set(s-1,stack.get(s-1)+stack.get(s));
            s--;

        }
        else if(comandos.get(i).getInstrucao2().trim().equals("SUB")){
            stack.set(s-1,stack.get(s-1)-stack.get(s));
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("MULT")){
            stack.set(s-1,stack.get(s-1)*stack.get(s));
            s--;
        }
        else if(comandos.get(i).getInstrucao2().trim().equals("DIVI")){
            stack.set(s-1,stack.get(s-1)/stack.get(s));
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("INV")){
            stack.set(s,-stack.get(s));
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("AND")){
            if(stack.get(s-1)==1 && stack.get(s)==1){
                stack.set(s-1,1);
            } else {
                stack.set(s-1,0);
            }
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("OR")){
            if(stack.get(s-1)==1 || stack.get(s)==1){
                stack.set(s-1,1);
            } else {
                stack.set(s-1,0);
            }
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("NEG")){
            stack.set(s,1-stack.get(s));
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("CME")){
            if(stack.get(s-1)< stack.get(s)){
                stack.set(s-1,1);
            } else {
                stack.set(s-1,0);
            }
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("CMA")){
            if(stack.get(s-1) > stack.get(s)){
                stack.set(s-1,1);
            } else {
                stack.set(s-1,0);
            }
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("CEQ")){
            if(stack.get(s-1)== stack.get(s)){
                stack.set(s-1,1);
            } else {
                stack.set(s-1,0);
            }
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("CDIF")){
            if(stack.get(s-1)!= stack.get(s)){
                stack.set(s-1,1);
            } else {
                stack.set(s-1,0);
            }
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("CMEQ")){
            if(stack.get(s-1)<= stack.get(s)){
                stack.set(s-1,1);
            } else {
                stack.set(s-1,0);
            }
            s--;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("CMAQ")){
            if(stack.get(s-1)>= stack.get(s)){
                stack.set(s-1,1);
            } else {
                stack.set(s-1,0);
            }
            s--;
        }


        else if(comandos.get(i).getInstrucao2().trim().equals("JMP")){
            int p=Integer.parseInt(comandos.get(i).getInstrucao3().replace(" ",""));
            i=p;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("JMPF")){
            int p=Integer.parseInt(comandos.get(i).getInstrucao3().replace(" ",""));
            if(stack.get(s)==0)
                i=p;
            else{
                // i=i+1;//ver
            }
            s=s-1;
        }

        else if(comandos.get(i).getInstrucao2().trim().equals("RETURN")){
            i=stack.get(s);
            s--;
        } else if(comandos.get(i).getInstrucao2().trim().equals("HLT")){
            proximoPasso.setVisible(false);
            endOfExecutionAlert.showAndWait();
        }
        atualizarPilhaTela();
        i++;
    }

    public void rodar(){

        while(!comandos.get(i).getInstrucao2().trim().equals("HLT")){
           analisarComando();
        }
        endOfExecutionAlert.showAndWait();
    }
}
