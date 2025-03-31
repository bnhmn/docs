import pathlib
from enum import Enum, auto

from data_source import DataSource
from models import Lap, DataPoint

INPUT_FILE = pathlib.Path(__file__).parent / "oschersleben_laps.csv"
ORIGIN = DataPoint(x_m=0, y_m=0, time_ms=0)
EPSILON = 20


class State(Enum):
    ENTERING_ORIGIN = auto()
    INSIDE_ORIGIN = auto()
    EXITING_ORIGIN = auto()
    OUTSIDE_ORIGIN = auto()


class RaceLapAnalyzer:
    def __init__(self, data_src=DataSource(INPUT_FILE)):
        self.data_src = data_src
        self.laps = []
        self.curr_lap = Lap()
        self.best_lap = Lap()

    def analyze(self):
        last_distance = float("inf")

        for point in self.data_src:
            curr_distance = point.distance(ORIGIN)
            if curr_distance <= EPSILON:
                curr_state = State.ENTERING_ORIGIN if last_distance > EPSILON else State.INSIDE_ORIGIN
            else:
                curr_state = State.OUTSIDE_ORIGIN if last_distance > EPSILON else State.EXITING_ORIGIN

            if curr_state == State.ENTERING_ORIGIN:
                # Current Lap is over
                if self.curr_lap < self.best_lap:  # Check if current lap was faster than previous
                    self.best_lap = self.curr_lap
                self.next_lap()

            self.curr_lap.points.append(point)
            print(f"lap={self.curr_lap.number}"
                  f" | x={point.x_m:8.3f}m"
                  f" | y={point.y_m:8.3f}m"
                  f" | time={self.curr_lap.get_elapsed_time(point):10.3f}ms"
                  f" | gap={self.compute_gap_time(point):10.4f}ms"
                  f" | origin_distance={curr_distance:7.3f}m"
                  f" | state={str(curr_state):21}")
            last_distance = curr_distance

    def next_lap(self):
        """
        Create a new Lap.
        """
        lap_number = self.laps[-1].number + 1 if self.laps else 1
        self.curr_lap = Lap(number=lap_number)
        self.laps.append(self.curr_lap)
        print("\nLap", lap_number)

    def compute_gap_time(self, point: DataPoint) -> float:
        """
        Compute the time offset between best Lap and current Lap at a point's position.
        """
        best_lap_time = self.best_lap.get_elapsed_time(point)
        curr_lap_time = self.curr_lap.get_elapsed_time(point)
        if best_lap_time >= 0:
            return best_lap_time - curr_lap_time
        else:
            return 0

    def print_summary(self):
        """
        Print Lap summary.
        """
        print()
        for lap in self.laps:
            print(f"Lap {lap.number} has {len(lap.points):3} data points and took {lap.lap_time:16.10f} [ms]")

        print()
        print(f"Best lap is Lap number: {self.best_lap.number}")


if __name__ == "__main__":
    analyzer = RaceLapAnalyzer()
    analyzer.analyze()
    analyzer.print_summary()
