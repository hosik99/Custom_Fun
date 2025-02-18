
export const handleInputChange = (setter) => {
    return (e) => {
      const { name, value } = e.target;
      setter((prevState) => ({
        ...prevState,
        [name]: value,
      }));
    };
};