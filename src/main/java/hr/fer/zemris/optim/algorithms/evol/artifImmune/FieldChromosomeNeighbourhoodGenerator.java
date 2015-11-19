package hr.fer.zemris.optim.algorithms.evol.artifImmune;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.optim.NeighbourhoodGenerator;
import hr.fer.zemris.optim.Pool;
import hr.fer.zemris.optim.evol.chromosome.PermutationChromosome;
import hr.fer.zemris.optim.rng.IRNG;
import hr.fer.zemris.optim.rng.RNG;

public class FieldChromosomeNeighbourhoodGenerator<T extends PermutationChromosome>
		implements NeighbourhoodGenerator<T> {

	private Pool<T> pool;

	public FieldChromosomeNeighbourhoodGenerator() {
	}

	public FieldChromosomeNeighbourhoodGenerator(Pool<T> pool) {
		this.pool = pool;
	}

	@Override
	public Iterable<T> neighbourhood(T seed) {
		return new NG<T>(seed);
	}

	@Override
	public Iterable<T> neighbourhoodPart(T seed, int size) {
		int len = seed.field.length;
		Set<Point> set = new HashSet<>();
		IRNG rand = RNG.getRNG();
		Point p2 = new Point();
		for (int i = 0; i < size; i++) {
			Point p1 = new Point(rand.nextInt(len), rand.nextInt(len));
			p2.x = p1.y;
			p2.y = p1.x;
			while (set.contains(p1) || set.contains(p2)) {
				p1.x = rand.nextInt(len);
				p1.y = rand.nextInt(len);
				p2.x = p1.y;
				p2.y = p1.x;
			}
			set.add(p2);
		}
		return new NGPart<>(seed, set, pool);
	}

	// public static void main(String[] args) {
	// FieldChromosome one = new FieldChromosome(8);
	// for (int i = 0; i < 8; i++) {
	// one.field[i] = i;
	// }
	// int i = 0;
	// for (FieldChromosome s : new FieldChromosomeNeighbourhoodGenerator()
	// .neighbourhood(one)) {
	// i++;
	// System.out.println(Arrays.toString(s.field));
	// System.out.println(i);
	// }
	// System.out.println(i);
	// }

	private static class NGPart<T extends PermutationChromosome> implements
			Iterable<T> {
		private T seed;
		private Collection<Point> swaps;
		private Pool<T> pool;

		public NGPart(T seed, Collection<Point> swaps, Pool<T> pool) {
			super();
			this.seed = seed;
			this.swaps = swaps;
			this.pool = pool;
		}

		@Override
		public Iterator<T> iterator() {
			return new MyIterator2<T>(seed, swaps, pool);
		}
	}

	private static class MyIterator2<T extends PermutationChromosome> implements
			Iterator<T> {
		private PermutationChromosome seed;
		private Iterator<Point> it;
		private Pool<T> pool;
		private Collection<Point> swaps;

		public MyIterator2(PermutationChromosome seed, Collection<Point> swaps,
				Pool<T> pool) {
			super();
			this.seed = seed;
			this.it = swaps.iterator();
			this.pool = pool;
			this.swaps = swaps;
		}

		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public T next() {
			Point p = it.next();
			T tmp;
			if (pool != null) {
				tmp = pool.getElement();
				for (int j = 0; j < 256; j++) {
					tmp.field[j] = seed.field[j];
				}
			} else {
				tmp = (T) seed.clone();
			}
			int hlp = tmp.field[p.x];
			tmp.field[p.x] = tmp.field[p.y];
			tmp.field[p.y] = hlp;
			return tmp;
		}

	}

	private static class NG<T extends PermutationChromosome> implements Iterable<T> {

		private T seed;

		public NG(T seed) {
			super();
			this.seed = seed;
		}

		@Override
		public Iterator<T> iterator() {
			return new MyIterator<T>(seed);
		}

		private static class MyIterator<T extends PermutationChromosome> implements
				Iterator<T> {
			private PermutationChromosome seed;
			private int i;
			private int j;
			private int len;

			public MyIterator(PermutationChromosome seed) {
				super();
				this.seed = seed;
				this.len = seed.field.length;
				j = 1;
				i = 0;
			}

			@Override
			public boolean hasNext() {
				if (i >= len - 2 && j >= len - 1) {
					return false;
				}
				return true;
			}

			@Override
			public T next() {
				if (j == len) {
					i++;
					j = i + 1;
				}
				@SuppressWarnings("unchecked")
				T res = (T) seed.clone();
				int tmp = res.field[i];
				res.field[i] = res.field[j];
				res.field[j] = tmp;
				j++;
				return res;
			}

		}

	}
}
