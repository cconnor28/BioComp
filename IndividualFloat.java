/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biocomp;

import java.util.Arrays;

/**
 *
 * @author Charlie
 */
public class IndividualFloat {

  float gene[];
  private int fitness;

  public IndividualFloat(float[] gene, int fitnesse) {
    this.gene = gene;
    this.fitness = fitnesse;
  }

  @Override
  public String toString() {
    return "Individual{" + "gene=" + Arrays.toString(gene) + ", fitnesse=" + fitness + '}';
  }

  /**
   * @return the gene
   */
  public float[] getGene() {
    return gene;
  }

  /**
   * @param gene the gene to set
   */
  public void setGene(float[] gene) {
    this.gene = gene;
  }

  /**
   * @return the fitness
   */
  public int getFitness() {
    return fitness;
  }

  /**
   * @param fitness the fitness to set
   */
  public void setFitness(int fitness) {
    this.fitness = fitness;
  }
}
