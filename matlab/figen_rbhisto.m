hold all
cdfplot(ldmk_den_2km);
cdfplot(ldmk_den_3km);
cdfplot(ldmk_den_4km);
%cdfplot(ldmk_den_5km);
title('');
legend('400km','300km','200km');
xlabel('Landmark Density within Radius','FontSize',15);
ylabel('Cumulative Probability','FontSize',15);