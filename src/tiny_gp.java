/*
 * Program:   tiny_gp.java
 *
 * Author:    Riccardo Poli (email: rpoli@essex.ac.uk)
 *
 */

import java.util.*;
import java.io.*;

import static java.lang.Math.pow;

public class tiny_gp {
    private int BEST;
    private double[] fitness;
    public char[][] pop;
    private static Random rd = new Random();
    private static final int ADD = 110, SUB = 111, MUL = 112, DIV = 113,
    //            SIN = 114, COS = 115,
//            LN = 114,
    FSET_START = ADD, FSET_END = DIV;
    private static double minrandom, maxrandom;
    private static double[] x = new double[FSET_START];
    private static char[] program;
    private static int PC;
    private static int varnumber, fitnesscases, randomnumber;
    private static double fbestpop = 0.0, favgpop = 0.0;
    private static long seed;
    private static double avg_len;
    private static final int MAX_LEN = 10000, POPSIZE = 100000, DEPTH = 3, GENERATIONS = 2, TSIZE = 2;
    public static final double PMUT_PER_NODE = 0.05, CROSSOVER_PROB = 0.9;
    private static double[][] targets;

    private double run() { /* Interpreter */
        char primitive = program[PC++];
        if (primitive < FSET_START)
            return (x[primitive]);
        switch (primitive) {
            case ADD:
                return (run() + run());
            case SUB:
                return (run() - run());
            case MUL:
                return (run() * run());
//            case LN : {
//                double num = run();
//                if ( num <= 0.001 )
//                    return -num;
//                else
//                    return Math.log( num );
//            }
//            case SIN : return Math.sin(run());
//            case COS : return Math.cos(run());
            case DIV: {
                double num = run(), den = run();
                if (Math.abs(den) <= 0.001)
                    return (num);
                else
                    return (num / den);
            }
        }
        return (0.0); // should never get here
    }

    private int traverse(char[] buffer, int buffercount) {
        if (buffer[buffercount] < FSET_START)
            return (++buffercount);

        switch (buffer[buffercount]) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
//            case LN:
//            case SIN:
//            case COS:
                return (traverse(buffer, traverse(buffer, ++buffercount)));
        }
        return (0); // should never get here
    }

    private void setup_fitness(String fname) {
        try {
            int i, j;
            String line;

            BufferedReader in = new BufferedReader(new FileReader(fname));
            line = in.readLine();
            StringTokenizer tokens = new StringTokenizer(line);
            varnumber = Integer.parseInt(tokens.nextToken().trim());
            randomnumber = Integer.parseInt(tokens.nextToken().trim());
            minrandom = Double.parseDouble(tokens.nextToken().trim());
            maxrandom = Double.parseDouble(tokens.nextToken().trim());
            fitnesscases = Integer.parseInt(tokens.nextToken().trim());
            targets = new double[fitnesscases][varnumber + 1];

            if (varnumber + randomnumber >= FSET_START)
                System.out.println("too many variables and constants");

            for (i = 0; i < fitnesscases; i++) {
                line = in.readLine();
                tokens = new StringTokenizer(line);
                for (j = 0; j <= varnumber; j++) {
                    targets[i][j] = Double.parseDouble(tokens.nextToken().trim());
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Please provide a data file");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("ERROR: Incorrect data format");
            System.exit(0);
        }
    }

    private double fitness_function(char[] Prog) {
        int i = 0, len;
        double result, fit = 0.0;

        len = traverse(Prog, 0);
        for (i = 0; i < fitnesscases; i++) {
            for (int j = 0; j < varnumber; j++)
                x[j] = targets[i][j];
            program = Prog;
            PC = 0;
            result = run();
            fit += Math.abs(result - targets[i][varnumber]);
        }
        return (-fit);
    }

    private int grow(char[] buffer, int pos, int max, int depth) {
        char prim = (char) rd.nextInt(2);
        int one_child;

        if (pos >= max)
            return (-1);

        if (pos == 0)
            prim = 1;

        if (prim == 0 || depth == 0) {
            prim = (char) rd.nextInt(varnumber + randomnumber);
            buffer[pos] = prim;
            return (pos + 1);
        } else {
            prim = (char) (rd.nextInt(FSET_END - FSET_START + 1) + FSET_START);
            switch (prim) {
                case ADD:
                case SUB:
                case MUL:
                case DIV:
//                case LN:
//                case SIN:
//                case COS:
                    buffer[pos] = prim;
                    one_child = grow(buffer, pos + 1, max, depth - 1);
                    if (one_child < 0)
                        return (-1);
                    return (grow(buffer, one_child, max, depth - 1));
            }
        }
        return (0); // should never get here
    }

    private int print_indiv(char[] buffer, int buffercounter) {
        int a1 = 0, a2;
        if (buffer[buffercounter] < FSET_START) {
            if (buffer[buffercounter] < varnumber)
                System.out.print("X" + (buffer[buffercounter] + 1) + " ");
            else
                System.out.print(x[buffer[buffercounter]]);
            return (++buffercounter);
        }
        switch (buffer[buffercounter]) {
            case ADD:
                System.out.print("(");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" + ");
                break;
            case SUB:
                System.out.print("(");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" - ");
                break;
            case MUL:
                System.out.print("(");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" * ");
                break;
            case DIV:
                System.out.print("(");
                a1 = print_indiv(buffer, ++buffercounter);
                System.out.print(" / ");
                break;
//            case SIN:
//                System.out.print( "sin( ");
//                a1=print_indiv( buffer, ++buffercounter );
//                System.out.print( " )");
//                return a1;
//            case COS:
//                System.out.print( "cos( ");
//                a1=print_indiv( buffer, ++buffercounter );
//                System.out.print( " )");
//                return a1;
//            case LN:
//                System.out.print( "ln( ");
//                a1=print_indiv( buffer, ++buffercounter );
//                System.out.print( " )");
//                return a1;
        }
        a2 = print_indiv(buffer, a1);
        System.out.print(")");
        return (a2);
    }

    public int save_best_indiv(String fname, char[] buffer, int buffercounter) throws IOException {
        int a1 = 0, a2;
        FileWriter myWriter;
        if (buffer[buffercounter] < FSET_START) {
            myWriter = new FileWriter(fname, true);
            if (buffer[buffercounter] < varnumber)
                myWriter.write("X" + (buffer[buffercounter] + 1) + " ");
            else
                myWriter.write(String.valueOf(x[buffer[buffercounter]]));
            myWriter.close();
            return (++buffercounter);
        }
        switch (buffer[buffercounter]) {
            case ADD:
                myWriter = new FileWriter(fname, true);
                myWriter.write("(");
                myWriter.close();
                a1 = save_best_indiv(fname, buffer, ++buffercounter);
                myWriter = new FileWriter(fname, true);
                myWriter.write(" + ");
                myWriter.close();
                break;
            case SUB:
                myWriter = new FileWriter(fname, true);
                myWriter.write("(");
                myWriter.close();
                a1 = save_best_indiv(fname, buffer, ++buffercounter);
                myWriter = new FileWriter(fname, true);
                myWriter.write(" - ");
                myWriter.close();
                break;
            case MUL:
                myWriter = new FileWriter(fname, true);
                myWriter.write("(");
                myWriter.close();
                a1 = save_best_indiv(fname, buffer, ++buffercounter);
                myWriter = new FileWriter(fname, true);
                myWriter.write(" * ");
                myWriter.close();
                break;
            case DIV:
                myWriter = new FileWriter(fname, true);
                myWriter.write("(");
                myWriter.close();
                a1 = save_best_indiv(fname, buffer, ++buffercounter);
                myWriter = new FileWriter(fname, true);
                myWriter.write(" / ");
                myWriter.close();
                break;
//            case SIN:
//                myWriter = new FileWriter(fname,true);
//                myWriter.write("sin( ");
//                myWriter.close();
//                a1=save_best_indiv(fname, buffer, ++buffercounter );
//                myWriter = new FileWriter(fname,true);
//                myWriter.write( " )");
//                myWriter.close();
//                return a1;
//            case COS:
//                myWriter = new FileWriter(fname,true);
//                myWriter.write( "cos( ");
//                myWriter.close();
//                a1=save_best_indiv(fname, buffer, ++buffercounter );
//                myWriter = new FileWriter(fname,true);
//                myWriter.write( " )");
//                myWriter.close();
//                return a1;
//            case LN:
//                myWriter = new FileWriter(fname,true);
//                myWriter.write( "ln( ");
//                myWriter.close();
//                a1=save_best_indiv(fname, buffer, ++buffercounter );
//                myWriter = new FileWriter(fname,true);
//                myWriter.write( " )");
//                myWriter.close();
//                return a1;

        }
        a2 = save_best_indiv(fname, buffer, a1);
        myWriter = new FileWriter(fname, true);
        myWriter.write(")");
        myWriter.close();
        return (a2);
    }

    private static char[] buffer = new char[MAX_LEN];

    private char[] create_random_indiv(int depth) {
        char[] ind;
        int len;

        len = grow(buffer, 0, MAX_LEN, depth);

        while (len < 0)
            len = grow(buffer, 0, MAX_LEN, depth);

        ind = new char[len];

        System.arraycopy(buffer, 0, ind, 0, len);
        return (ind);
    }

    private char[][] create_random_pop(int n, int depth, double[] fitness) {
        char[][] pop = new char[n][];
        int i;

        for (i = 0; i < n; i++) {
            pop[i] = create_random_indiv(depth);
            fitness[i] = fitness_function(pop[i]);
        }
        return (pop);
    }


    private void stats(double[] fitness, char[][] pop, int gen) {
        int i, best = rd.nextInt(POPSIZE);
        int node_count = 0;
        fbestpop = fitness[best];
        favgpop = 0.0;

        for (i = 0; i < POPSIZE; i++) {
            node_count += traverse(pop[i], 0);
            favgpop += fitness[i];
            if (fitness[i] > fbestpop) {
                best = i;
                fbestpop = fitness[i];
            }
        }
        avg_len = (double) node_count / POPSIZE;
        favgpop /= POPSIZE;
        System.out.print("Generation=" + gen + " Avg Fitness=" + (-favgpop) +
                " Best Fitness=" + (-fbestpop) + " Avg Size=" + avg_len +
                "\nBest Individual: ");

//        try {
//            FileWriter myWriter = new FileWriter("process2.txt",true);
//            myWriter.write( gen+","+(-favgpop)+
//                ","+(-fbestpop)+","+avg_len+
//                "\n");
//            myWriter.close();
//        } catch (IOException e) {
//            System.err.println(e);
//        }

        print_indiv(pop[best], 0);
        BEST = best;
        System.out.print("\n");
        System.out.flush();
    }

    private int tournament(double[] fitness, int tsize) {
        int best = rd.nextInt(POPSIZE), i, competitor;
        double fbest = -1.0e34;

        for (i = 0; i < tsize; i++) {
            competitor = rd.nextInt(POPSIZE);
            if (fitness[competitor] > fbest) {
                fbest = fitness[competitor];
                best = competitor;
            }
        }
        return (best);
    }

    private int negative_tournament(double[] fitness, int tsize) {
        int worst = rd.nextInt(POPSIZE), i, competitor;
        double fworst = 1e34;

        for (i = 0; i < tsize; i++) {
            competitor = rd.nextInt(POPSIZE);
            if (fitness[competitor] < fworst) {
                fworst = fitness[competitor];
                worst = competitor;
            }
        }
        return (worst);
    }

    private char[] crossover(char[] parent1, char[] parent2) {
        int xo1start, xo1end, xo2start, xo2end;
        char[] offspring;
        int len1 = traverse(parent1, 0);
        int len2 = traverse(parent2, 0);
        int lenoff;

        xo1start = rd.nextInt(len1);
        xo1end = traverse(parent1, xo1start);

        xo2start = rd.nextInt(len2);
        xo2end = traverse(parent2, xo2start);

        lenoff = xo1start + (xo2end - xo2start) + (len1 - xo1end);

        offspring = new char[lenoff];

        System.arraycopy(parent1, 0, offspring, 0, xo1start);
        System.arraycopy(parent2, xo2start, offspring, xo1start,
                (xo2end - xo2start));
        System.arraycopy(parent1, xo1end, offspring,
                xo1start + (xo2end - xo2start),
                (len1 - xo1end));

        return (offspring);
    }

    private char[] mutation(char[] parent, double pmut) {
        int len = traverse(parent, 0), i;
        int mutsite;
        char[] parentcopy = new char[len];

        System.arraycopy(parent, 0, parentcopy, 0, len);
        for (i = 0; i < len; i++) {
            if (rd.nextDouble() < pmut) {
                mutsite = i;
                if (parentcopy[mutsite] < FSET_START)
                    parentcopy[mutsite] = (char) rd.nextInt(varnumber + randomnumber);
                else
                    switch (parentcopy[mutsite]) {
                        case ADD:
                        case SUB:
                        case MUL:
                        case DIV:
//                        case SIN:
//                        case COS:
//                        case LN:
                            parentcopy[mutsite] =
                                    (char) (rd.nextInt(FSET_END - FSET_START + 1)
                                            + FSET_START);
                    }
            }
        }
        return (parentcopy);
    }

    private void print_parms() {
        System.out.print("-- TINY GP (Java version) --\n");
        System.out.print("SEED=" + seed + "\nMAX_LEN=" + MAX_LEN +
                "\nPOPSIZE=" + POPSIZE + "\nDEPTH=" + DEPTH +
                "\nCROSSOVER_PROB=" + CROSSOVER_PROB +
                "\nPMUT_PER_NODE=" + PMUT_PER_NODE +
                "\nMIN_RANDOM=" + minrandom +
                "\nMAX_RANDOM=" + maxrandom +
                "\nGENERATIONS=" + GENERATIONS +
                "\nTSIZE=" + TSIZE +
                "\n----------------------------------\n");
    }

    public tiny_gp(String fname, long s) {
        fitness = new double[POPSIZE];
        seed = s;
        if (seed >= 0)
            rd.setSeed(seed);
        setup_fitness(fname);
        for (int i = 0; i < FSET_START; i++)
            x[i] = (maxrandom - minrandom) * rd.nextDouble() + minrandom;
        pop = create_random_pop(POPSIZE, DEPTH, fitness);
    }

    private void evolve() {
        int gen = 0, indivs, offspring, parent1, parent2, parent;
        double newfit;
        char[] newind;
        print_parms();
        stats(fitness, pop, 0);
        for (gen = 1; gen < GENERATIONS; gen++) {
            if (fbestpop > -1e-5) {
                System.out.print("PROBLEM SOLVED\n");
                return;
            }
            for (indivs = 0; indivs < POPSIZE; indivs++) {
                if (rd.nextDouble() < CROSSOVER_PROB) {
                    parent1 = tournament(fitness, TSIZE);
                    parent2 = tournament(fitness, TSIZE);
                    newind = crossover(pop[parent1], pop[parent2]);
                } else {
                    parent = tournament(fitness, TSIZE);
                    newind = mutation(pop[parent], PMUT_PER_NODE);
                }
                newfit = fitness_function(newind);
                offspring = negative_tournament(fitness, TSIZE);
                pop[offspring] = newind;
                fitness[offspring] = newfit;
            }
            stats(fitness, pop, gen);
        }
        System.out.print("PROBLEM *NOT* SOLVED\n");
    }

    private void save(int i, int j) {
        try {

            String fSolName = "solution/solution" + i + "/solution_" + i + "_" + j + ".txt";
            File yourFile = new File(fSolName);
            yourFile.createNewFile();
            FileWriter myWriter = new FileWriter(fSolName, false);
            myWriter.write("");
            myWriter.close();
            save_best_indiv(fSolName, pop[BEST], 0);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        for (int i=1; i<2; i++) {
//            for (int j = 2;j<3;j++){
//                String fname = "data/data" + i + "/data_" + i + "_" + j + ".dat";
//                System.out.println(fname);
//                long s = -1;
//
//                if (args.length == 2) {
//                    s = Integer.valueOf(args[0]).intValue();
//                    fname = args[1];
//                }
//                if (args.length == 1) {
//                    fname = args[0];
//                }
//
//                tiny_gp gp = new tiny_gp(fname, s);
//                gp.evolve();
//                gp.save(i,j);
//            }
//        }
//    }
        String fname = "data/data1/data_1_1.dat";
        System.out.println(fname);
        long s = -1;

        if (args.length == 2) {
            s = Integer.valueOf(args[0]).intValue();
            fname = args[1];
        }
        if (args.length == 1) {
            fname = args[0];
        }

        tiny_gp gp = new tiny_gp(fname, s);
        gp.evolve();
    }
}