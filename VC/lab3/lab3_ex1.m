function [out] = lab3_ex1(in)
% efficient average filter
out = in;
offset = 2;
[r,c] = size(in);

col_array = in(1,1:5);

for i = offset + 1: (r-offset)
    for j = offset + 1: (c-offset)
        a = mean(col_array);
        out(i,j) = a;
        if(j < c - offset)
            col_array = [col_array(2:end), in(i,j + 3)];
        end
    end
end
end

