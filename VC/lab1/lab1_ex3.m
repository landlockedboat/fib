[X, Y] = meshgrid(-15:1:15);

F = 2*cos(sqrt(X.*X + Y.*Y)/(2*pi));

F(F < 0) = 0;

surf(X,Y,F);