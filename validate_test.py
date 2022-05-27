from code_pipeline.visualization import RoadTestVisualizer
from code_pipeline.tests_generation import RoadTestFactory
from code_pipeline.executors import MockExecutor
import numpy as np
import sys

def get_road_points(array):
    road_points = []
    for i in range(len(array)):
        road_points.append(
            (
                array[i][0], #* self.map_size,
                array[i][1]# * self.map_size
            )
        )
    return road_points

test_executor = MockExecutor(result_folder="results", time_budget=1e10, map_size=500, road_visualizer=RoadTestVisualizer(map_size=500))
X = np.asarray(sys.argv[1].split(','),dtype=float)
Y = np.asarray(sys.argv[2].split(','),dtype=float)
final = np.column_stack((X, Y))
road_points = get_road_points(final)
the_test = RoadTestFactory.create_road_test(road_points)
is_valid, validation_message = test_executor.validate_test(the_test)
if is_valid:
    print("Test seems valid")
else:
    print(f"Test is invalid: {validation_message}")



