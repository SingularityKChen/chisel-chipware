module CW_add #(parameter wA=4) (
  input [wA-1:0] A, B,
  input CI,
  output [wA-1:0] Z,
  output CO
);
assign {CO,Z} = A + B + CI;
endmodule : CW_add
