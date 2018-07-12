function[newImage] = lab2_ex4(I1, I2, tform)
% image stitching

[r1, c1] = size(I1);

I2 = imwarp(I2, tform);

[r2, c2] = size(I2);

fr = r2;
fc = c1 + c2;

newImage = cast(zeros(fr, fc), 'uint8');

newImage(1:r1, 1:c1) = I1;
newImage(1:fr, c1 + 1:fc) = I2;
end
