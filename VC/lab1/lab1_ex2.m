t = 0: 2*pi/30 :2*pi;
y = -cos(t);
y(y < 0) = 0;

plot(t, y);