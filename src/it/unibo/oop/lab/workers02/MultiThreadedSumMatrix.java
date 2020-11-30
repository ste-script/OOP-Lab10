package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;


public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    public MultiThreadedSumMatrix(final int threads) {
        this.nthread = threads;
    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length % nthread + matrix.length / nthread;
        final List<Double> list = new ArrayList<>();
        for (final double[] d : matrix) {
            for (int i = 0; i < matrix.length; i++) {
                list.add(d[i]);
                System.out.println(d[i]);
            }
        }
        System.out.println(list);
        /*
         * Build a stream of workers
         */
        return DoubleStream.iterate(0, start -> (int) start + size)
                .limit(nthread)
                .mapToObj(start -> new Worker(list, (int) start, size))
                // Start them
                .peek(Thread::start)
                // Join them
                .peek(MultiThreadedSumMatrix::joinUninterruptibly)
                 // Get their result and sum
                .mapToLong(Worker::getResult)
                .sum();
    }

    private static void joinUninterruptibly(final Thread target) {
        var joined = false;
        while (!joined) {
            try {
                target.join();
                joined = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Worker extends Thread {
        private final List<Double> list;
        private final int startpos;
        private final int nelem;
        private long res;

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
        public long getResult() {
            return this.res;
        }

    }

}
