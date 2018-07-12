[X, Y] = meshgrid(-15:1:15);

% Mateixa funcio que a l'exercici anterior
F = 2*cos(sqrt(X.*X + Y.*Y)/(2*pi));

F = F*1.5/2;

F2 = repmat(F, 2, 2);

[X2, Y2] = meshgrid(0:1:61);

F2(F2 < 0) = 0;

surf(X2,Y2,F2);
axis tight