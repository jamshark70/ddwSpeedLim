// 2021 h. james harkins
// similar to [speedlim] in Max

SpeedLim {
	var <>time = 0, <>action, value, <>clock;
	var state = \idle;
	var lastOutTime = 0, lastInTime = 0;

	*new { |time = 0, action, initVal, clock(SystemClock)|
		^super.newCopyArgs(time, action, initVal, clock)
	}

	value { |... invals|
		value = invals;
		lastInTime = clock.seconds;
		if(state == \idle) {
			if(this.ready) { this.doAction };
			state = \pending;
			clock.sched(time, {
				if(this.ready) { this.doAction };
				state = \idle;
			});
		};
	}

	ready {
		^lastInTime > lastOutTime
		and: { clock.seconds - lastOutTime >= time }
	}

	doAction {
		action.valueArray(value);
		lastOutTime = clock.seconds;
	}
}
