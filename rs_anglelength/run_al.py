import random
import math
import logging
from matplotlib import pyplot as plt
import numpy as np
from scipy import interpolate
from code_pipeline.tests_generation import RoadTestFactory
from code_pipeline.beamng_executor import BeamngExecutor
from code_pipeline.executors import MockExecutor
from code_pipeline.visualization import RoadTestVisualizer
from scipy.interpolate import make_interp_spline, interp1d
import sys

class AngleLengthGenerator:

    def __init__(self, executor: None, map_size: None):
        self.executor = executor
        self.map_size = map_size
        self.min_coordinate = 20
        self.max_coordinate = 480

    def draw_spline(self, final):
        x = final[:, 0]
        y = final[:, 1]
        tck, u = interpolate.splprep([x, y], k=3)
        xnew, ynew = interpolate.splev(np.linspace(0, 1, 100), tck, der=0)
        plt.plot(x, y, 'o', xnew, ynew)
        plt.legend(['data', 'spline'])
        plt.show()
        plt.pause(100000)
        return

    def draw_roadpoints(self, road_points):
        plt.axis([0, 500, 0, 500])
        for i in range(0, len(road_points)):
            plt.scatter(road_points[i][0], road_points[i][1])
        plt.show()
        plt.pause(10)
        return

    def three_points(self, x, y, angle, length):
        endy = y + length * math.sin(math.radians(angle))
        endx = x + length * math.cos(math.radians(angle))
        ny = y + length * 0
        nx = x + length * 1
        if self.check_boundaries(endx, endy) and self.check_boundaries(nx, ny):
            final = np.array([[nx, ny], [x, y], [endx, endy]])
            return final
        else:
            return None

    def more_points(self, x, y, angle, length):
        n_arr = np.zeros(shape=(length, 2))
        end_arr = np.zeros(shape=(length, 2))
        for l in range(1, length + 1):
            endy = y + l * math.sin(math.radians(angle))
            endx = x + l * math.cos(math.radians(angle))
            end_arr[l-1] = [endx, endy]
        for l in range(1, length + 1):
            ny = y + l * 0
            nx = x + l * 1
            n_arr[l-1] = [nx, ny]
        return np.concatenate((end_arr, n_arr, [[x, y]]))

    def get_road_points(self, array):
        road_points = []
        for i in range(len(array)):
            road_points.append(
                (
                    array[i][0], #* self.map_size,
                    array[i][1]# * self.map_size
                )
            )
        return road_points

    def check_boundaries(self, x, y):
        if int(x) < self.min_coordinate or int(x) > self.max_coordinate or int(y) < self.min_coordinate or int(y) > self.max_coordinate:
            return False
        else:
            return True

    def angle_range(self, x, y, length):
        arr = []
        for a in range(45, 135):
            (endx, endy) = (x + length * math.cos(math.radians(a)), y + length * math.sin(math.radians(a)))
            if self.check_boundaries(endx, endy):
                arr = np.append(arr, a)
        return arr

    def build_road(self, road):
        done = False
        cnt = 0
        end = 15
        (x, y) = (random.uniform(self.min_coordinate, self.max_coordinate),
                  random.uniform(self.min_coordinate, self.max_coordinate))
        print("X E Y", x, y)
        arr = [[x, y]]
        while not done:
            length = random.randint(50, 100)
            print("L: ", length)
            angle_range = self.angle_range(x, y, length)
            secure_random = random.SystemRandom()
            if len(angle_range):
                angle = secure_random.choice(angle_range)
                #angle = random.choice(angle_range)#random.randint(45, 150)  # meno di 45 o più di 315 -> the road is too sharp
                print("BETA: ", angle)
                (x, y) = (x + length * math.cos(math.radians(angle)), y + length * math.sin(math.radians(angle)))
                # print("NUOVI X E Y", (x, y))
                # print("LENGTH E ANGLE", length, angle)
                if self.check_boundaries(x, y):
                    arr = np.append(arr, [[x, y]], axis=0)
                    cnt = cnt + 1
                    print(cnt)
                    if cnt == road:
                        print("CNT == ROAD", cnt==road)
                        done = True
                else:
                    end = end - 1
                    if end == 0:
                        done = True
            else:
                print("VUOTO: ")
                print(angle_range)
                done = True
        return arr

    def get_max_oob_percentage(self, execution_data):
        max_oob_percentage = 0
        for record in execution_data:
            logging.info(f"Processing record with oob: {record.oob_percentage}")
            if record.oob_percentage > max_oob_percentage:
                logging.debug(f"New oob max: {record.oob_percentage}")
                max_oob_percentage = record.oob_percentage
        # logging.debug(f"Returning oob max: {max_oob_percentage}")
        return max_oob_percentage

    def start(self):

        #test_executor = MockExecutor(result_folder="results", time_budget=1e10, map_size=500, road_visualizer=RoadTestVisualizer(map_size=500))



        #(x, y) = (60, 180)#(random.uniform(self.min_coordinate, self.max_coordinate), random.uniform(self.min_coordinate, self.max_coordinate))
        #angle = 100#random.randint(45, 315) #meno di 45 o più di 315 -> the road is too sharp
        #length =200# random.randint(100, 200)
        #final = self.three_points(x, y, angle, length)
        with open('C:\\Users\\kikki\\PycharmProjects\\progetto\\application\\chosenPoint.txt','r') as f:
            lines = f.readlines()

        X = np.asarray(str(lines[0]).split(','), dtype=float)
        Y = np.asarray(str(lines[1]).split(','), dtype=float)
        speed = int(np.asarray(str(lines[2])))
        test_executor = BeamngExecutor(generation_budget=10000, execution_budget=10000, time_budget=10000,
                                       result_folder="C:\\Users\\kikki\\PycharmProjects\\progetto\\results", map_size=1000, beamng_home="C:\\Users\\kikki\\BeamNG",
                                       beamng_user="C:\\Users\\kikki\\BeamNG_user",
                                       road_visualizer=RoadTestVisualizer(map_size=1000), max_speed_in_kmh=speed)
        final = np.column_stack((X, Y))
        print(final)
        if final is None:
            print("Errore nella costruzione dei punti")
        else:
            road_points = self.get_road_points(final)
            # road_points = self.get_prova();
            # self.draw_spline(road_points)
            # self.draw_roadpoints(road_points)

            the_test = RoadTestFactory.create_road_test(road_points)
            is_valid, validation_message = test_executor.validate_test(the_test)
            if is_valid:
                print("Test seems valid")
                test_outcome, description, execution_data = test_executor.execute_test(the_test)
                print("oob")
                for record in execution_data:
                    print(record.oob_percentage);
                print("end")
                if test_outcome == "ERROR":
                    print("Test seemed valid, but test outcome was ERROR. Negative reward.")
                elif test_outcome == "PASS":
                    print("Test is valid and passed.")
                elif test_outcome == "FAIL":
                    print("Test is valid and failed.")
                for record in execution_data:
                    print(record.oob_percentage);
            else:
                print(f"Test is invalid: {validation_message}")

