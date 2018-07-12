function [outputImage] = ex1(inputImage)
% circle the pixel with biggest contrast with its neighbors
im = inputImage;
[r, c] = size(inputImage);
maxContrast = 0;
mCX = -1;
mCY = -1;

for x = 2:(r-1)
    for y = 2:(c-1)
        if mod(x, r) ~= 1
            pi = max(1, round(y - 1));
            c = im(x, y) - im(y, pi);
            if maxContrast < c
                mCX = x;
                mCY = y;
                maxContrast = c;
            end
        end
    end
end

outputImage = insertShape(im, 'circle', [mCX, mCY, 5]);
end

