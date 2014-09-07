close all
clear
clc

load ('pcr.mat');

X=train_input;
Y=train_target;

[rowX,colX] = size(train_input);
[rowY,colY] = size(train_target);

YXcorr=corr(train_target, train_input);
Xcorr=corr(train_input);

[U, S, V]= svd(train_input);
D= S(1:colX,:);
L=diag(D);
L=100*L/sum(L);

figure, plot(L)
xlabel('Principal Component'), ylabel('Weights')
title('Scree Plot')

figure, plot(cumsum(L))
xlabel('Principal Component'), ylabel('Weights')
title('CUM  Plot')

N_factors=14;
Z=U*S(:,1:N_factors)*V(1:N_factors,1:N_factors);

bz=(Z'*Z)\Z'*Y;

Yp=Z*bz;

figure, plot(Y(:,1),Y(:,1),'-b',Y(:,1),Yp(:,1),'.r')
xlabel('Actual'), ylabel('Predicted')
title('Y1')
legend('Actual', 'Predicted')

figure, plot(Y(:,2),Y(:,2),'-b',Y(:,2),Yp(:,2),'.r')
xlabel('Actual'), ylabel('Predicted')
title('Y2')
legend('Actual', 'Predicted')

err=Y-Yp;

figure, plot(err)
xlabel(''), ylabel('Error (ms)^2')
title('Erro')












