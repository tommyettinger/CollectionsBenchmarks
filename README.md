# CollectionsBenchmarks
Update to Leo Lewis' Java collection benchmarks with graphs

This is derived from https://github.com/leolewis/sandbox/blob/master/src/org/leo/benchmark/Benchmark.java ,
which was used in this blog post http://lewisleo.blogspot.com/2012/08/java-collections-performance.html .
It has been updated to use Maven to handle dependencies (Javolution seems to be not present on Maven Central
in any working fashion, so it isn't tested; SquidLib is tested and Guava can be added later).
