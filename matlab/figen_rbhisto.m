% This figure shows the probability of points has a surrounding less than a
% given count
clc;
clear;
load('data_rb_dens.mat');

hold all
grid on
%omecdf(ldmk_den_2);
omecdf(ldmk_den_3);
%omecdf(ldmk_den_4);
%omecdf(ldmk_den_5);
title('');
%legend('200km','300km','400km','500km');
legend('300km');
xlabel('Landmark Count within Radius','FontSize',15);
ylabel('Percentage','FontSize',15);
%set(gca,'XTick',[0 50 100 200 300 400 500 700 1000]);
xlim([0 1000]);