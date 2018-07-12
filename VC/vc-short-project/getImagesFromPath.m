function [ images ] = getImagesFromPath(path)

imf = dir(strcat(path, '*.pgm'));

I = imread(strcat(path, imf(1).name));
[sx, sy] = size(I);

n = length(imf);
images = zeros([n, sx, sy]);

for index = 1 : n
    namef = imf(index).name;
    I = imread(strcat(path, namef));
	images(index,:,:) = I;
end

end