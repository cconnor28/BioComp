/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biocomp;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Charlie
 */
public class GeneticAlgorithmData2 {

    static int N = 70; //Number of genes
    static int P = 75; //Population size 
    static int G = 250; //Number of generations
    static double M = 0.010; //Mutation rate (Max 3 dp)
    static int F = 0; //Best average fitness
    static int Generation = 0; //Generation with best average fitness
    static Individual Fittest = new Individual(null, 0); //Fittest individual from current Population
    static Individual[] Population = new Individual[P]; //Array of the Population
    static ArrayList<String[]> Fitnesses = new ArrayList<>(); //Arraylist of generation & average fitness
    static int[] Variables = new int[448]; //Array of the Variables from the input file
    static DataFile[] DataSet = new DataFile[64]; // Array of the data file
    static int RuleLength = 7; // Length of varibles and outputs in a rule

    public static void main(String[] args) throws Exception {

        initialise();
        evaluate(0);
        display(0);
        for (int i = 0; i < G; i++) {
            selection();
            crossover();
            mutation();
            evaluate(i + 1);
        }

        System.out.println("Best Generation: " + Generation + " out of " + G);
        System.out.println("Average Fitness = " + F);

        try (
                // writer
                FileWriter writer = new FileWriter("Data 2 Output.csv")) {

            // headers
            writer.write("Best Fitness,Average Fitness\n");
            writer.flush();

            // data
            for (String[] arr : Fitnesses) {
                String appender = "";
                for (String s : arr) {
                    writer.write(appender + s);
                    appender = ",";
                }
                writer.write("\n");
                writer.flush();
            }
        }

    }

    public static int randInt(int max, int min) {//Returns a random integer between a min & max value entered

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static void initialise() {
        int v = 0;
        //Reads in the input file
        Scanner sc = new Scanner(GeneticAlgorithmData2.class.getResourceAsStream("data2.txt"));

        while (sc.hasNextInt()) {
            Variables[v] = sc.nextInt();
            v++;
        }

        // Creates the Data File objects from the array created from the data file
        int d = 0;
        int condition[];
        int output = 0;

        for (int i = 0; i < Variables.length / RuleLength; i++) {
            condition = new int[RuleLength - 1];

            for (int j = 0; j < RuleLength; j++) {
                if (j < RuleLength - 1) {
                    condition[j] = Variables[d];
                    d++;
                } else {
                    output = Variables[d];
                    d++;
                }
            }
            DataSet[i] = new DataFile(condition, output);
        }

        //Initalises the first genration randomly
        for (int i = 0; i < P; i++) {
            int[] gene = new int[N];
            for (int j = 0; j < N; j++) {
                if (((j + 1) % RuleLength) == 0) {
                    gene[j] = randInt(1, 0);
                } else {
                    gene[j] = randInt(2, 0);
                }
            }
            Population[i] = new Individual(gene, 0);
        }

    }

    public static void fitness() {//Calculates the fitness for each individual within a Population
        int fitness;
        int[] condition = new int[RuleLength - 1];
        int output = 0;
        int gene[];
        for (int i = 0; i < P; i++) {
            fitness = 0;
            gene = Population[i].getGene();
            for (DataFile DataSet1 : DataSet) {
                int[] data = DataSet1.getVariable();
                for (int j = 0; j < N / RuleLength; j++) {
                    int g = j * RuleLength;
                    for (int k = 0; k < RuleLength; k++) {
                        if (k < RuleLength - 1) {
                            condition[k] = gene[g];
                            g++;
                        } else {
                            output = gene[g];
                        }
                    }

                    int m = 0;
                    for (int k = 0; k < RuleLength - 1; k++) {
                        if (condition[k] == 2) {
                            m++;
                        } else if (condition[k] == data[k]) {
                            m++;
                        }
                    }
                    if (m == RuleLength - 1) {
                        int o = DataSet1.getOutput();
                        if (output == o) {
                            fitness++;
                        }
                        break;
                    }
                }
            }
            Population[i].setFitness(fitness);
        }
    }

    public static void fittest() {// Finds the fiittest individual within a Population 
        int fitnesse;
        int highest = 0;
        int h = 0;

        for (int i = 0; i < P; i++) {
            fitnesse = Population[i].getFitness();
            if (fitnesse > highest) {
                highest = fitnesse;
                h = i;
            }
        }
        Fittest = Population[h];
    }

    public static void selection() {
        Individual[] offspring = new Individual[P];

        for (int i = 0; i < P; i++) {
            int parent1 = randInt(P - 1, 0);
            int parent2 = randInt(P - 1, 0);
            if (Population[parent1].getFitness() >= Population[parent2].getFitness()) {
                offspring[i] = Population[parent1];
            } else {
                offspring[i] = Population[parent2];
            }
        }
        Population = offspring;
    }

    public static void crossover() {
        Individual[] offspring = new Individual[P];

        for (int i = 0; i < P; i++) {
            int point = randInt(N, 0);
            int rand1 = randInt(P - 1, 0);
            int rand2 = randInt(P - 1, 0);
            int[] parent1 = Population[rand1].getGene();
            int[] parent2 = Population[rand2].getGene();
            int[] offspring1 = new int[N];

            for (int j = 0; j < N; j++) {
                if (j < point) {
                    offspring1[j] = parent1[j];
                } else {
                    offspring1[j] = parent2[j];
                }
            }
            offspring[i] = new Individual(offspring1, 0);
        }

        Population = offspring;
    }

    public static void mutation() {
        int probability = (int) (M * 1000);

        for (int i = 0; i < P; i++) {
            int[] gene = Population[i].getGene();
            for (int j = 0; j < N; j++) {
                int k = randInt(1000, 0);
                if (k < probability) {
                    if (gene[j] == 0) {
                        gene[j] = 1;
                    } else {
                        gene[j] = 0;
                    }
                }
            }
            Population[i].setGene(gene);
        }
    }

    public static void evaluate(int g) {
        int fitness = 0;
        int lowestFitness;
        int lowest = N;
        int l = 0;

        //Calls the fitness function
        fitness();

        //Finds the fittest in initial population 
        if (g == 0) {
            fittest();
        }

        //Finds the memeber with the lowest fitness in the population
        for (int i = 0; i < P; i++) {
            lowestFitness = Population[i].getFitness();
            if (lowestFitness < lowest) {
                lowest = lowestFitness;
                l = i;
            }
        }

        //Replaces the lowest fittnese within an offspring Population with the highest from the generation before
        if (g > 0) {
            Population[l] = Fittest;
        }

        //Calculates the total fitness for a population
        for (int i = 0; i < P; i++) {
            fitness = fitness + Population[i].getFitness();
        }

        //Calculates the average fitness for a population
        int average = fitness / P;

        //Adds the best & average fitness to an array list
        Fitnesses.add(new String[]{Integer.toString(Fittest.getFitness()), Integer.toString(average)});

        //Checks if the current popultion is the fiitest and stores it's details
        if (average > F) {
            F = average;
            Generation = g;
        }

        //Finds the fittest in the current population 
        fittest();

    }

    public static void display(int g) {//Displays information about the current population
        int fitness = 0;

        System.out.println("Generation:" + g);

        for (int i = 0; i < P; i++) {
            //System.out.println("ID:" + i + " " + Population[i].toString());
            fitness = fitness + Population[i].getFitness();
        }

        int average = fitness / P;

        System.out.println("Average fitnesse = " + average);
        System.out.println("");

    }

} //class
