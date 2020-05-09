# Computational math lab5
## Решение обыкновенных дифференциальных уравнений многошаговым методом Милна

Пользователю предоставляется возможность ввести:
+ дифференциальное уравнение
+ начальное условие (x0, y0) для задачи Коши
+ правую границу отрезка
+ желаемую точность

Программа рассчитывает набор точек (x, y) частного решения введенного дифура и по этому набору строит график найденного частного решения.
Также программа выводит получившийся шаг и количество вычисленных точек, а так же координаты этих точек.

*P.S. Так как для работы метода Милна необхоимы первые 4 значения, то для поиска 3х из них (еще одно содержится в начальном условии) используется одношаговый метод Рунге-Кутта четвертого порядка.*

*P.P.S. На каждой итерации высчитывается погрешность и, если эта погрешность больше желаемой, шаг уменьшается в два раза и все значения расчитываются заново.*