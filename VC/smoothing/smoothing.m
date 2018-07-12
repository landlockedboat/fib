function [outputImage] = smoothing(I,smoothRatio)
% gray image smothing function
outputImage = I;
if (mod(smoothRatio,2) == 1)
    [r,c] = size(I);
    offset = ((smoothRatio - 1) / 2);
    for i = offset+1:(r-offset)
        for j = offset+1:(c-offset)
            val = I(i,j);
            if val == 0 || val == 255
                outputImage(i,j) = smoothPixel(I,offset,smoothRatio,i,j);
            end
        end
    end
else
    disp("ERROR: smoothRatio not an odd number");
end

end

