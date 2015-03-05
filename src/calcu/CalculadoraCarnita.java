/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calcu;

import java.util.Scanner;
import java.util.Stack;
import stackadt.StackADT;
import stackadt.Unchecked;
import stackadt.miPila;

/**
 *
 * @author MRUIZGA
 */
public class CalculadoraCarnita {

    public miPila calculadorita;

    public CalculadoraCarnita() {
        this.calculadorita = new miPila();
    }

    public double evaluaPostFijo(String operacion) throws Unchecked {
        miPila calculadorita = new miPila();
         double resultado = 0, numero1 = 0, numero2 = 0;
        char operador;
        String lector;
        Scanner sc = new Scanner(operacion);

        while (sc.hasNext()) {
            lector = sc.next();
            operador = lector.charAt(0);
            if (operador != '/' && operador != '*' && operador != '─' && operador != '+' ) {
                calculadorita.push(Double.parseDouble(lector));
            } else {
                if (calculadorita.isEmpty()) {
                    throw new Unchecked("Syntax ERROR");
                }
                numero2 = (Double) calculadorita.pop();
                numero1 = (Double) calculadorita.pop();
                switch (operador) {
                    case '/':
                        if (numero2 == 0) {
                            throw new Unchecked("Math ERROR");
                        } else {
                            resultado = numero1 / numero2;
                        }
                        break;
                    case '*':
                        resultado = numero1 * numero2;
                        break;
                    case '─':
                        resultado = numero1 - numero2;
                        break;
                    case '+':
                        resultado = numero1 + numero2;
                }
                calculadorita.push(resultado);
            }
        }
        if (calculadorita.size() != 1) {
            throw new Unchecked("Syntax ERROR");
        }

        return (Double) calculadorita.peek();
    }

    public String infijoAPostfijo(String expr) {
        StringBuilder postfix = new StringBuilder(),creadorNumeros=new StringBuilder();
        String token;
        Scanner sc;
        miPila pila = new miPila();

        if (pila.evaluaParentesis(expr)) {
            try {
                for(int i=0;i<expr.length();i++) {
                    token = expr.charAt(i)+"";
                    if (isOperator(token)) {
                        postfix.append(" "+creadorNumeros);
                        while (!pila.isEmpty()
                                && !pila.peek().equals("(")
                                && opGreaterEqual((String) pila.peek(), token)) {
                            postfix.append(" " + pila.pop());
                        }
                        pila.push(token);
                        creadorNumeros=new StringBuilder();
                    } else if (token.equals("(")) {
                        pila.push(token);
                        creadorNumeros=new StringBuilder();
                    } else if (token.equals(")")) {
                        while (!(pila.peek().equals("("))) {
                            postfix.append(" " + pila.pop());
                        }
                        pila.pop(); // paréntesis que abre correspondiente
                        creadorNumeros=new StringBuilder();
                    } else // token is operand
                    {   
                        creadorNumeros.append(token);
                    }
                    if(i==expr.length()-1){
                       postfix.append(" "+creadorNumeros); 
                    }
                }
                
                while (!pila.isEmpty()) {
                    postfix.append(" " + pila.pop());
                }
            } catch (Unchecked ese) {
                postfix = new StringBuilder("Invalid Expression");
                System.err.println(ese);
            }

        } else {
            postfix = new StringBuilder("Invalid Expression");
            System.err.println("Paréntesis no balanceados");
        }

        return postfix.toString();
    }

    static boolean isOperator(String token) {
        return (token.equals("+") || token.equals("─")
                || token.equals("*") || token.equals("/"));
    }

    private static boolean opGreaterEqual(String tope, String actual) {
        boolean result = false;
        if ((tope.equals("*") || tope.equals("/"))
                && (actual.equals("+") || actual.equals("─"))) {
            result = true;
        }
        return result;
    }
    
    public static boolean isNumber(String n){
        try{
            Integer.parseInt(n);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }

    public static void main(String[] args) {
        CalculadoraCarnita calcu = new CalculadoraCarnita();
        String operador = calcu.infijoAPostfijo("3855─-263");

        System.out.print("\n"+operador);
        System.out.print("\n" + calcu.evaluaPostFijo(operador) + "\n" + "\n");
    }
}
