package gameMechanics;

import java.time.chrono.IsoChronology;

import math.Vector2d;

@Deprecated
public class Collision {
	Collider c1, c2;
	Vector2d hit;
	public Collision(Collider c1, Collider c2, Vector2d hit){
		this.c1 = c1;
		this.c2 = c2;
		this.hit = hit;
	}
}
