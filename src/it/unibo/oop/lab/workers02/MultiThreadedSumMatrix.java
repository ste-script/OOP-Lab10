package it.unibo.oop.lab.workers02;
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
                .mapToDouble(startpos -> {
                    double res = 0;
                    System.out.println("Working from position " + startpos + " to position " + (startpos + size - 1));
                    for (int i = startpos; i < matrix.length && i < startpos + size; i++) {
                        for (final double d : matrix [i]) {
                            res += d;
                        }
                    }
                    return res;
                })
                .sum();
    }


}
