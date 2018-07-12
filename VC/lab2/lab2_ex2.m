function lab2_ex2(inputImage, fractions)
% generate histogram of an image

[r, c] = size(inputImage);

histogram = zeros(fractions, 1);

for x = 1:(r)
    for y = 1:(c)
        amm = (cast(inputImage(x,y), 'double'));
        index = floor((amm/255.0) * fractions);
        if(index <= 0)
            index = 1;
        end
        histogram(index) = histogram(index) + 1;
    end
    bar(histogram);
end

end

