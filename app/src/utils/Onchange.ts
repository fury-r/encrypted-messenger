export const onChange = (
  e: any,
  setState: React.Dispatch<React.SetStateAction<any>>,
  key: string
) => {
  e
    ? setState({
        ...setState,
        [key]: e,
      })
    : setState(e);
};
