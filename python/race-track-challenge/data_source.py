from __future__ import annotations

import csv
from typing import Iterable

from models import DataPoint


class DataSource:

    def __init__(self, file_path):
        self.file_path = file_path

    def __iter__(self) -> Iterable[DataPoint]:
        with open(self.file_path, encoding="utf-8", newline="") as csv_file:
            csv_reader = csv.DictReader(csv_file)
            for row in csv_reader:
                yield DataPoint(
                    x_m=float(row["x_m"]),
                    y_m=float(row["y_m"]),
                    time_ms=float(row["time_ms"]),
                )
