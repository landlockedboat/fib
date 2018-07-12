function [ O ] = unsalpepperFilter( I )
    n = I(3, 3);
    if n == 0 || n == 255
        O = mean(mean(I));
    else
        O = n;
    end
end