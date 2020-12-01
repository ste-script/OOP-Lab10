package it.unibo.oop.lab.workers02;

import java.util.List;
import java.util.stream.IntStream;


public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    public MultiThreadedSumMatrix(final int threads) {
        this.nthread = threads;
    }

    @Override
    public double sum(final double[][] matrix) {

        final int size = matrix.length % nthread + matrix.length / nthread;
        /*
         * Build a stream of workers
         */
        return  IntStream.iterate(0, start -> (int) start + size)
                .limit(nthread)
                .parallel()
                .map(startpos -> {
                    System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
                    for (int i = startpos; i < list.size() && i < startpos + nelem; i++) {
                        this.res += this.list.get(i);
                    }
                })
                .sum();
    }


    }

    private static class Worker extends Thread {
        private final List<Double> list;
        private final int startpos;
        private final int nelem;
        private double res;

        /**
         * Build a new worker.
         * 
         * @param list
         *            the list to sum
         * @param startpos
         *            the initial position for this worker
         * @param nelem
         *            the no. of elems to sum up for this worker
         */
        Worker(final List<Double> list, final int startpos, final int nelem) {
            super();
            this.list = list;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < list.size() && i < startpos + nelem; i++) {
                this.res += this.list.get(i);
            }
        }

        /**
         * Returns the result of summing up the integers within the list.
         * 
         * @return the sum of every element in the array
         */
        public double getResult() {
            return this.res;
        }

    }

}
