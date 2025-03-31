from __future__ import annotations

import math
from dataclasses import dataclass, field
from functools import total_ordering
from typing import List


@dataclass
class DataPoint:
    x_m: float
    y_m: float
    time_ms: float

    def distance(self, other: DataPoint) -> float:
        """
        Compute distance from this DataPoint to another DataPoint.
        """
        return math.sqrt((other.x_m - self.x_m) ** 2 + (other.y_m - self.y_m) ** 2)


@total_ordering
@dataclass
class Lap:
    number: int = None
    points: List[DataPoint] = field(default_factory=list)

    @property
    def lap_time(self) -> float:
        """
        The lap time in [ms].
        """
        if self.points:
            return self.points[-1].time_ms - self.points[0].time_ms
        else:
            return float("inf")

    def get_elapsed_time(self, point: DataPoint) -> float:
        """
        Compute the elapsed time at a point's position (x_m, y_y).
        """
        min_distance = float("inf")
        min_index = -1
        # Find the lap's data point which position is closest to the given point and return its time
        # TODO: Add interpolation
        for index, lap_point in enumerate(self.points):
            distance = lap_point.distance(point)
            if distance < min_distance:
                min_distance = distance
                min_index = index

        if min_index >= 0:
            return self.points[min_index].time_ms - self.points[0].time_ms
        else:
            return -1

    def __gt__(self, other: Lap):
        return isinstance(other, Lap) and self.lap_time > other.lap_time
