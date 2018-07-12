[x y] = meshgrid(-5:0.1:5);
z = (x.^2 + y - 5).^2 + (x + y.^2 - 9).^2;
fun = @(x)(x(1).^2 + x(2) - 5).^2 + (x(1) + x(2).^2 - 9).^2;
x0 = [0,0];
xr = fminsearch(fun, x0)